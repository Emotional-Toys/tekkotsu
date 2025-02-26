//-*-c++-*-
#ifndef INCLUDED_SketchIndices_h
#define INCLUDED_SketchIndices_h

#include <iosfwd> // forward declaration for ostream
#include <ext/hash_set>

#include "SketchTypes.h"

namespace DualCoding {

class SketchSpace;
template<class T> class Sketch;

//! Table of indices of sketch elements; parent of the Region class
class SketchIndices {
 public:
  typedef __gnu_cxx::hash_set<uint, __gnu_cxx::hash<uint>, std::equal_to<uint> > SketchIndicesTable;
  typedef SketchIndicesTable::const_iterator CI;

  SketchIndicesTable table;
  
  //! Constructor
  SketchIndices() : table() {};

  //! Destructor
  virtual ~SketchIndices() {};
  
  //! Operator for indirection based on passed in Sketch.
  //! Returns a new SketchIndices based on applying the indirection.
  const SketchIndices operator[] (const Sketch<uint>& indirection) const;
  
  //! Returns the result of adding the elements of another SketchIndices
  //! to the current SketchIndices
  const SketchIndices operator+ (const SketchIndices& other) const;
  
  //! Returns the result of removing the elements of another SketchIndices
  //! from the current SketchIndices.
  //! This is much more computationally expensive than addition, so try
  //! to minimize usage by distribution law: x-y-z=x-(y+z)
  const SketchIndices operator- (const SketchIndices& other) const;

  //! Adds indices of non-zero pixels.
  void addIndices(const Sketch<bool>& im);

  //! Removes any indices from SketchIndices which are outside the bounds of the SketchSpace
  void trimBounds(const SketchSpace &space);
  
private:
};

//! prints out SketchIndices for debugging
std::ostream& operator<< (std::ostream& out, const SketchIndices &s);

} // namespace

#endif
